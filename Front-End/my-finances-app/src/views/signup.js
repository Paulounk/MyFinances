import React from "react";
import { withRouter } from "react-router-dom";
import Card from "../components/card";
import FormGroup from "../components/form-group";
import UserService from "../app/service/userService";
import { messageError, messageSuccess, messageAlert } from '../components/toastr'


class SignUp extends React.Component {

    state = {
        name: '',
        email: '',
        password: '',
        confirmPassword: ''
    }

    constructor() {
        super();
        this.service = new UserService();
    }

    validate(){
        const msgs = []

        if(!this.state.name){
            msgs.push('The name field is required.')
        }

        if(!this.state.email){
            msgs.push('The e-mail field is required.')
        }else if(!this.state.email.match(/.+@.+\..+/) ){
            msgs.push('Enter a valid e-email!')
        }

        if(!this.state.password || !this.state.confirmPassword){
            msgs.push('Confirm your password!')
        }else if (this.state.password !== this.state.confirmPassword){
            msgs.push('Passwords do not match!')
        }

        return msgs;
    }

    save = () => {

        const msgs = this.validate();

        if(msgs && msgs.length > 0){
            msgs.forEach( (msg, index) => {
                messageAlert(msg)
            });
            return false;
        }

        const user = {
            name: this.state.name,
            email: this.state.email,
            password: this.state.password
        }

        this.service.saveUser(user)
            .then(response => {
                messageSuccess('Successfully registered user! Make your login.')
                this.props.history.push('/login')
            }).catch(error => {
                messageError(error.response.data)
            })
    }

    cancel = () => {
        this.props.history.push('/login')
    }

    render() {
        return (
            <div className="container">
                <Card title="Sign up">
                    <div className="row">
                        <div className="col-lg-12">
                            <div className="bs-component">

                                <FormGroup label="Name *" htmlFor="inputName">
                                    <input type="name" id="inputName" name="name" className="form-control" value={this.state.name}
                                        onChange={e => this.setState({ name: e.target.value })} />
                                </FormGroup>

                                <FormGroup label="Email *" htmlFor="inputEmail">
                                    <input type="email" id="inputEmail" name="email" className="form-control" value={this.state.email}
                                        onChange={e => this.setState({ email: e.target.value })} />
                                </FormGroup>

                                <FormGroup label="Password *" htmlFor="inputPassword">
                                    <input type="password" id="inputPassword" name="password" className="form-control" value={this.state.password}
                                        onChange={e => this.setState({ password: e.target.value })} />
                                </FormGroup>

                                <FormGroup label="Confirm Password *" htmlFor="inputConfirmPassword">
                                    <input type="password" id="inputConfirmPassword" name="confirmpassword" className="form-control" value={this.state.confirmPassword}
                                        onChange={e => this.setState({ confirmPassword: e.target.value })} />
                                </FormGroup>

                                <button onClick={this.save} type="button" className="btn btn-outline-success">Save</button>
                                <button onClick={this.cancel} type="button" className="btn btn-outline-danger">Cancel</button>
                            </div>
                        </div>
                    </div>
                </Card>
            </div>
        )
    }
}


export default withRouter(SignUp);