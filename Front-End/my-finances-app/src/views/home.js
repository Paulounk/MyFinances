import React from "react";
import UserService from "../app/service/userService";
import { AuthContext } from '../main/authProvider';

import Currency from "currency-formatter";


class Home extends React.Component {

    state = {
        balance: 0
    }

    constructor(){
        super();
        this.userService = new UserService();
    }

    componentDidMount(){
        const userAcess = this.context.authenticatedUser;

        this.userService
            .getBalanceByUser(userAcess.id)
            .then(response => {
                this.setState({ balance: response.data})
            }).catch(error => {
                console.error(error.response)
            })
    }

    render() {
        return (
            <div className="container bg-light p-5">
                <div className="jumbotron">
                    <h1 className="display-3">Welcome!</h1>
                    <p className="lead">This is your finance system.</p>
                    <p className="lead">Your balance for the current month is <span className="balance"> {Currency.format(this.state.balance, { locale: 'pt-BR' })}</span> </p> 
                    <hr className="my-4"/>
                    <p>This is your administrative area, use one of the menus or buttons below to navigate the system.</p>
                    <p className="lead">
                        <a className="btn btn-outline-success btn-lg" href="#/sign-up" role="button"><i className="pi pi-user"></i>Sign-Up</a>
                        <a className="btn btn-outline-info btn-lg" href="#/entries-registration" role="button"><i className="pi pi-money-bill"></i>Register Releases</a>
                    </p>
                </div>
            </div>
        )
    }
}

Home.contextType = AuthContext

export default Home