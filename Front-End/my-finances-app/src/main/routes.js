import React from "react";

import Home from '../views/home'
import Login from "../views/login";
import SignUp from "../views/signup";

import { Route, Switch, HashRouter} from 'react-router-dom'

function Routes(){
    return(
        <HashRouter>
            <Switch>    
                <Route path='/home' component={Home}/>
                <Route path='/login' component={Login}/>
                <Route path='/sign-up' component={SignUp}/>
            </Switch>
        </HashRouter>
    )
}

export default Routes