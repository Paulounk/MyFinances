import React from "react";
import { withRouter } from 'react-router-dom'
import Card from '../components/card';
import FormGroup from "../components/form-group";

import UserService from "../app/service/userService";
import {messageAlert} from '../components/toastr'
import { AuthContext } from '../main/authProvider'

class Login extends React.Component {

    state = {
        email: '',
        password: ''
    }

    constructor(){
        super();
        this.service = new UserService();
    }

    login = () => {
        this.service.authenticate({
            email: this.state.email,
            password: this.state.password
        }).then(response => {
            this.context.logOn(response.data)
            this.props.history.push('/home')
       }).catch(error => {
            messageAlert(error.response.data)
       })
    }

    browseSignUp = () => {
        this.props.history.push('/sign-up')
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-6" style={{ position: 'relative', left: '300px' }}>
                        <div className="bs-docs-section">
                            <Card title="Login">
                                <div className="row">
                                    <div className="col-lg-12">
                                        <div className="bs-component">
                                            <fieldset>
                                                <FormGroup label="Email *" htmlFor="exampleInputEmail">
                                                    <input type="email" value={this.state.email} onChange={e => this.setState({ email: e.target.value })}
                                                        className="form-control" id="exampleInputEmail"/>
                                                </FormGroup>

                                                <FormGroup label="Password *" htmlFor="exampleInputPassword">
                                                    <input type="password" value={this.state.password}
                                                        onChange={e => this.setState({password: e.target.value})}
                                                        className="form-control" id="exampleInputPassword"/>
                                                </FormGroup>

                                                <button onClick={this.login} 
                                                        className="btn btn-outline-success">
                                                        <i className="pi pi-sign-in"></i> Login
                                                </button>
                                                <button onClick={this.browseSignUp} 
                                                        className="btn btn-outline-info">
                                                        <i className="pi pi-user-plus"></i> Sign Up
                                                        </button>

                                            </fieldset>
                                        </div>
                                    </div>
                                </div>
                            </Card>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

Login.contextType = AuthContext

export default withRouter(Login);