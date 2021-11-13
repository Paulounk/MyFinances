import React from 'react';

import AuthService from '../app/service/authService'
import ApiService from '../app/apiservice'
import jwt from 'jsonwebtoken'

export const AuthContext = React.createContext()
export const AuthConsumer = AuthContext.Consumer;
const AuthenticationProvider = AuthContext.Provider

class AuthProvider extends React.Component{

    state = {
        authenticatedUser: '',
        isAuthenticated: false
    }

    logOn = (tokenDTO) => {
        const token = tokenDTO.token
        const claims = jwt.decode(token)

        const user = {
            id: claims.userId, //Pegando os atributos do token definido no back-end "JwtServiceImpl"
            name: claims.name
        }

        AuthService.login(user, token)
        this.setState({isAuthenticated: true, authenticatedUser: user})
    }

    logOff = () => {
        AuthService.deleteAuthenticatedUser()
        this.setState({isAuthenticated: false, authenticatedUser: ''})
    }

    componentDidMount(){
        const isAuthenticated = AuthService.isAuthenticatedUser()

        if(isAuthenticated){
            const user = AuthService.refreshSession();
            this.setState({
                isAuthenticated: true, 
                authenticatedUser: user})
        }
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