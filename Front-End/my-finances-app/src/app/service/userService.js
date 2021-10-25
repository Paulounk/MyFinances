import ApiService from "../apiservice";

class UserService extends ApiService{
    
    constructor(){
        super('/users')
    }

    authenticate(credentials){
        return this.post('/authenticate', credentials)
    }

    getBalanceByUser(id){
        return this.get(`/${id}/balance`);
        
    }
}

export default UserService