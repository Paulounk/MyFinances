import React from "react";

import landingPage from "../views/landingPage";
import Home from '../views/home'
import Login from "../views/login";
import SignUp from "../views/signup";
import SearchEntries from "../views/entries/search-entries";
import entriesRegistration from "../views/entries/entries-registration";
import { AuthConsumer } from '../main/authProvider'


import { Route, Switch, HashRouter, Redirect } from 'react-router-dom'

function RoutesAuthentication({ component: Component, isAuthenticatedUser, ...props }) {
    return (
        <Route exact {...props} render={(componentProps) => {
            if (isAuthenticatedUser) {
                return (
                    <Component {...componentProps} />
                )
            } else {
                return (
                    <Redirect to={{ pathname: '/login', state: { from: componentProps.location } }} />
                )
            }
        }} />
    )
}

function Routes(props) {
    return (
        <HashRouter>
            <Switch>
                <Route exact path='/' component={landingPage} />
                <Route exact path='/login' component={Login} />
                <Route exact path='/sign-up' component={SignUp} />
                <RoutesAuthentication isAuthenticatedUser={props.isAuthenticatedUser} path='/home' component={Home} />
                <RoutesAuthentication isAuthenticatedUser={props.isAuthenticatedUser} path='/search-entries' component={SearchEntries} />
                <RoutesAuthentication isAuthenticatedUser={props.isAuthenticatedUser} path='/entries-registration/:id?' component={entriesRegistration} />
            </Switch>
        </HashRouter>
    )
}



export default () => (
    <AuthConsumer>
        { (context) => (<Routes isAuthenticatedUser={ context.isAuthenticated }/>) }
    </AuthConsumer>
)