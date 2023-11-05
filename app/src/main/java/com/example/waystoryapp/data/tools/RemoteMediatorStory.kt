package com.example.waystoryapp.data.tools

import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.example.waystoryapp.data.api.ApiService
import com.example.waystoryapp.data.database.RemoteKeys
import com.example.waystoryapp.data.database.Entities
import com.example.waystoryapp.data.database.StoryDB


@OptIn(ExperimentalPagingApi::class)
class RemoteMediatorStory(
    private val token:String,
    private val db : StoryDB,
    private val apiService: ApiService,
): RemoteMediator<Int, Entities>()
{

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Entities>): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND ->{
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try{
            Log.i("RemoteMediatorStory", "load: $token")
            val responseData = apiService.getStory("Bearer $token",page,state.config.pageSize).listStory
            db.withTransaction {
                if(LoadType.REFRESH == loadType){
                    db.remoteKeysDao().deleteRemoteKeys()
                    db.StoryDao().deleteAllStory()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (LoadState.Loading.endOfPaginationReached) null else page + 1
                val keys = responseData?.map {
                    RemoteKeys(
                        id = it?.id!!,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                val newList = responseData?.map {
                    Entities(idStory = it?.id!!
                        , photoUrl = it.photoUrl!!
                        , createdAt = it.createdAt!!,
                        sender = it.name!!,
                        description = it.description!!,
                        lat = it.lat?.toDouble() ?: 0.0,
                        lon = it.lon?.toDouble() ?: 0.0
                    )
                }

                db.remoteKeysDao().insertAll(keys!!)
                db.StoryDao().insertStory(newList!!)

            }
            return MediatorResult.Success(endOfPaginationReached = LoadState.Loading.endOfPaginationReached)
        }catch(ex: Exception){
            ex.printStackTrace()
            Log.d("TAG", "load: ${ex.message}")
            return MediatorResult.Error(ex)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Entities>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            db.remoteKeysDao().getRemoteKeysId(data.idStory!!)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Entities>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            db.remoteKeysDao().getRemoteKeysId(data.idStory!!)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Entities>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.idStory?.let { id ->
                db.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}