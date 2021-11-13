import ApiService from "../apiservice";

import ErrorValidation from "../exception/ErrorValidation";

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

    saveUser(user){
        return this.post('', user);
    }

    validate(user){
        const erros = []

        if(!user.name){
            erros.push('The name field is required.')
        }

        if(!user.email){
            erros.push('The e-mail field is required.')
        }else if(!user.email.match(/.+@.+\..+/) ){
            erros.push('Enter a valid e-email!')
        }

        if(!user.password || !user.confirmPassword){
            erros.push('Confirm your password!')
        }else if (user.password !== user.confirmPassword){
            erros.push('Passwords do not match!')
        }

        if(erros && erros.length > 0){
            throw new ErrorValidation(erros)
        }
    }
}

export default UserService