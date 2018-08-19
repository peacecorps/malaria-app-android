package com.peacecorps.malaria.ui.user_profile.edit_profile;

import android.content.Context;
import android.text.TextUtils;

import com.peacecorps.malaria.data.AppDataManager;
import com.peacecorps.malaria.ui.base.BasePresenter;
import com.peacecorps.malaria.ui.user_profile.edit_profile.UserProfileContract.UserProfileMvpPresenter;
import com.peacecorps.malaria.ui.user_profile.edit_profile.UserProfileContract.UserProfileMvpView;

/**
 * Created by Anamika Tripathi on 13/7/18.
 */
public class UserProfilePresenter<V extends UserProfileMvpView> extends BasePresenter<V> implements UserProfileMvpPresenter<V> {

    UserProfilePresenter(AppDataManager manager, Context context) {
        super(manager, context);
    }

    // sets values from preferences to the edit texts
    @Override
    public void setPreviousDetails() {
        getView().setInitialValuesIfAvailable(
                getDataManager().getUserName(),
                getDataManager().getUserEmail(),
                getDataManager().getUserAge(),
                getDataManager().getDrugPicked()
        );
    }
    @Override
    public void attachView(V view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    // called if values are valid on done button click, saves in shared preferences
    @Override
    public void setNewDetails(String name, String email, int age) {
        getDataManager().setUserName(name);
        getDataManager().setUserEmail(email);
        getDataManager().setUserAge(age);
    }

    // util function for testing text is empty of not
    @Override
    public boolean testIsEmpty(String text) {
        return TextUtils.isEmpty(text);
    }

    // if age is not empty & is >0 + <=100 return true else false
    @Override
    public boolean isAgeValid() {
        if(testIsEmpty(getView().getUserAge()))
            return false;
        // user age can either be empty or a number (input type is number in edit text)
        int age = Integer.valueOf(getView().getUserAge());
        return age > 0 && age <= 100;
    }

    // if email is not empty & matches email address pattern, return true else false
    @Override
    public boolean isEmailValid() {
        return !testIsEmpty(getView().getUserEmail()) && android.util.Patterns.EMAIL_ADDRESS.matcher(getView().getUserEmail()).matches();
    }

    // called when save button clicked, returns true only if age, name & error are valid.
    @Override
    public boolean checkError() {
        return getView().checkAgeError() && getView().checkNameError() && getView().checkEmailError();
    }
}