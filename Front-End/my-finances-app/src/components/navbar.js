import React from "react";
import NavBarItem from "./navbaritem";
import { AuthConsumer } from '../main/authProvider'

function Navbar(props) {

    return (

        <div className="navbar navbar-expand-lg fixed-top background-navbar">
            <div className="container">
                <a href="#/home" className="navbar-brand">My Finances</a>

                <button className="navbar-toggler" type="button" 
                    data-toggle="collapse" 
                    data-target="#navbarResponsive" 
                    aria-controls="navbarResponsive" 
                    aria-expanded="false" 
                    aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarResponsive">
                    <ul className="navbar-nav">
                        <NavBarItem render={props.isAuthenticatedUser} href="#/home" label="Home"/>
                        <NavBarItem render={props.isAuthenticatedUser} href="#/search-entries" label="Releases"/>
                        <NavBarItem render={props.isAuthenticatedUser} href="#/sign-up" label="Sign Up"/>
                        <NavBarItem render={props.isAuthenticatedUser} onClick={props.logOff} href="#/login" label="Logout"/>
                    </ul>
                </div>
            </div>
        </div>
    )
}
 export default () => (
    <AuthConsumer>
        {(context) => (
            <Navbar isAuthenticatedUser={context.isAuthenticated} logOff={context.logOff}/>
        )}
    </AuthConsumer>
 )