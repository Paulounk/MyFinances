import LocalStorageService from "./localstorageService";

export const AUTHENTICATED_USER = '_user_acess'

export default class AuthService{

    static isAuthenticatedUser(){
        const user = LocalStorageService.getItem(AUTHENTICATED_USER);
        return user && user.id
    }

    static deleteAuthenticatedUser(){
        LocalStorageService.deleteItem(AUTHENTICATED_USER);
    }

    static login(user){
        LocalStorageService.addItem(AUTHENTICATED_USER, user);
    }

    static getByAuthenticatedUser(){
        return LocalStorageService.getItem(AUTHENTICATED_USER);
    }
}