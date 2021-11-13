import LocalStorageService from "./localstorageService";

import jwt from 'jsonwebtoken';
import ApiService from "../apiservice";

export const AUTHENTICATED_USER = '_user_acess';
export const TOKEN = '_token_acess';

export default class AuthService{

    static isAuthenticatedUser(){
        const token = LocalStorageService.getItem(TOKEN)

        if(!token){
            return false;
        }

        const decodedToken = jwt.decode(token)
        const expiration = decodedToken.exp
        const isTokenInvalid = Date.now() >= (expiration * 1000)

        return !isTokenInvalid;
    }

    static deleteAuthenticatedUser(){
        LocalStorageService.deleteItem(AUTHENTICATED_USER);
        LocalStorageService.deleteItem(TOKEN);
    }

    static login(user, token){
        LocalStorageService.addItem(AUTHENTICATED_USER, user);
        LocalStorageService.addItem(TOKEN, token);
        ApiService.registerToken(token)
    }

    static getByAuthenticatedUser(){
        return LocalStorageService.getItem(AUTHENTICATED_USER);
    }

    static refreshSession(){
        const token = LocalStorageService.getItem(TOKEN)
        const user = AuthService.getByAuthenticatedUser()

        AuthService.login(user, token)

        return user
    }
}