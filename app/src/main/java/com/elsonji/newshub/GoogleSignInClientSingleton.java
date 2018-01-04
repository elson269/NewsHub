package com.elsonji.newshub;
//This class is created to ensure there is only one GoogleSignInClient instance across the app
// and it can be retrieved in NewsListActivity for the Logout function because this instance cannot be
// passed through intent.

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class GoogleSignInClientSingleton {
    private static GoogleSignInClientSingleton instance = null;

    private static GoogleSignInClient mGoogleSignInClient = null;

    protected GoogleSignInClientSingleton() {

    }

    public static GoogleSignInClientSingleton getInstance(GoogleSignInClient googleSignInClient) {
        if (instance == null) {
            instance = new GoogleSignInClientSingleton();
            if (mGoogleSignInClient == null)
                mGoogleSignInClient = googleSignInClient;
        }
        return instance;
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }
}
