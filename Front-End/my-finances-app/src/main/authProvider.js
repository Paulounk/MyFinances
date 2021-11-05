import React from 'react';

import AuthService from '../app/service/authService'

export const AuthContext = React.createContext()
export const AuthConsumer = AuthContext.Consumer;
const AuthenticationProvider = AuthContext.Provider

class AuthProvider extends React.Component{

    state = {
        authenticatedUser: '',
        isAuthenticated: false
    }

    logOn = (user) => {
        AuthService.login(user)
        this.setState({isAuthenticated: true, authenticatedUser: user})
    }

    logOff = () => {
        AuthService.deleteAuthenticatedUser()
        this.setState({isAuthenticated: false, authenticatedUser: ''})
    }


    render(){
        const contexto = {
            authenticatedUser: this.state.authenticatedUser,
            isAuthenticated: this.state.isAuthenticated,
            logOn: this.logOn,
            logOff : this.logOff
        }

        return(
            <AuthenticationProvider value={contexto}>
                {this.props.children}
            </AuthenticationProvider>
        )
    }
}

export default AuthProvider;