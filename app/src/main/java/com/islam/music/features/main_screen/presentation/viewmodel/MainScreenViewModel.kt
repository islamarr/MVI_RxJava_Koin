package com.islam.music.features.main_screen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.islam.music.common.data.DataResponse
import com.islam.music.common.view.BaseViewModel
import com.islam.music.common.view.NewBaseViewModel
import com.islam.music.features.main_screen.domain.usecases.MainScreenUseCase
import com.islam.music.features.main_screen.domain.usecases.SavedListLoaded
import com.islam.music.features.search.domain.entites.ArtistListLoaded
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val useCase: MainScreenUseCase) :
    NewBaseViewModel() {

    private val response = MutableLiveData<DataResponse<SavedListLoaded>>()
    val state: LiveData<DataResponse<SavedListLoaded>>
        get() = response

    init {
        fetch()
    }

    private fun fetch() {
        //  artistResponse.postValue(DataResponse.Loading)
        compositeDisposable.add(
            useCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userList ->
                    response.postValue(DataResponse.Success(userList))
                }, { throwable ->
                    response.postValue(DataResponse.Failure(throwable.message))
                })
        )
    }

}