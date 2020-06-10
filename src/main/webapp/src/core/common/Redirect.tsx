import React from 'react';
import Redirect from 'react-router-dom';

export default class RedirectService{
    public redirect(path : string){
        if(path)
            history.pushState("", "", path);
            history.go();
    }
}
