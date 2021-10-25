import React from "react";
import { withRouter } from "react-router-dom";
import Card from "../components/card";
import FormGroup from "../components/form-group";


class SignUp extends React.Component {

    state = {
        name: '',
        email: '',
        password: '',
        confirmPassword: ''
    }

    save = () => {
        console.log(this.state)
    }

    cancel = () => {
        this.props.history.push('/sign-in')
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