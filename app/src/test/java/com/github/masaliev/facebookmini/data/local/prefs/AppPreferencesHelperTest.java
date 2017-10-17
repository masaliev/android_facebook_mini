package com.github.masaliev.facebookmini.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.masaliev.facebookmini.MockHelper;
import com.github.masaliev.facebookmini.utils.Constants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by mbt on 10/17/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class AppPreferencesHelperTest {

    @Mock
    Context mContext;
    @Mock
    SharedPreferences mSharedPreferences;

    @Mock
    SharedPreferences.Editor mEditor;

    private PreferencesHelper mPreferencesHelper;

    @Before
    public void setUp() throws Exception {
        when(mContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mSharedPreferences);
        mPreferencesHelper = new AppPreferencesHelper(mContext, Constants.PREF_NAME);
    }

    @Test
    public void getCurrentUserToken() throws Exception {
        //Given
        when(mSharedPreferences.getString(anyString(), any()))
                .thenReturn(MockHelper.TOKEN);

        //When

        //Then
        assertEquals(MockHelper.TOKEN, mPreferencesHelper.getCurrentUserToken());
    }

    @Test
    public void setCurrentUserToken() throws Exception {
        //Given
        when(mSharedPreferences.edit())
                .thenReturn(mEditor);
        when(mEditor.putString(anyString(), any()))
                .thenReturn(mEditor);

        //When
        mPreferencesHelper.setCurrentUserToken(MockHelper.TOKEN);

        //Then
        verify(mEditor).putString(AppPreferencesHelper.PREF_KEY_TOKEN, MockHelper.TOKEN);
        verify(mEditor).apply();
    }

}