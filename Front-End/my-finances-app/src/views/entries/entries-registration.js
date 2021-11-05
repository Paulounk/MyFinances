import React from 'react'

import Card from '../../components/card'
import FormGroup from '../../components/form-group'
import SelectMenu from '../../components/select-menu'

import { withRouter } from 'react-router-dom'
import * as messages from '../../components/toastr'

import EntryService from '../../app/service/entryService'
import LocalStorageService from '../../app/service/localstorageService'

class EntriesRegistration extends React.Component {

    state = {
        id: 0,
        description: '',
        value: '',
        month: '',
        year: '',
        type: '',
        status: '',
        user: 0,
        updating: false
    }

    constructor() {
        super();
        this.service = new EntryService();
    }

    componentDidMount() {
        const params = this.props.match.params

        if (params.id) {
            this.service
                .getEntryById(params.id)
                .then(response => {
                    this.setState({ ...response.data, updating: true })
                })
                .catch(error => {
                    messages.messageError(error.response.data)
                })
        }
    }

    submit = () => {

        const userAcess = LocalStorageService.getItem('_user_acess')
        const { description, value, month, year, type } = this.state;
        const entry = { description, value, month, year, type, user: userAcess.id };

        try{
            this.service.validate(entry)
        }catch(error){
            const messagesErros = error.messages;
            messagesErros.forEach(msg => messages.messageAlert(msg));
            return false;
            
        }

        this.service
            .save(entry)
            .then(response => {
                this.props.history.push('/search-entries')
                messages.messageSuccess('Entry registered successfully!')
            }).catch(error => {
                messages.messageError(error.response.data)
            })
    }

    update = () => {

        const userAcess = LocalStorageService.getItem('_user_acess')
        const { description, value, month, year, status, type, id } = this.state;
        const entry = { description, value, month, year, status, type, id, user: userAcess.id };

        this.service
            .update(entry)
            .then(response => {
                this.props.history.push('/search-entries')
                messages.messageSuccess('Entry updated successfully!')
            }).catch(error => {
                messages.messageError(error.response.data)
            })
    }

    handleChange = (event) => {
        const value = event.target.value;
        const name = event.target.name;

        this.setState({ [name]: value })
    }

    render() {
        const types = this.service.getListTypes();
        const months = this.service.getListMonth();

        return (
            <div className="container">

                <Card title={this.state.updating ? 'Entry Update ' : 'Entry Registration'}>
                    <div className="row">
                        <div className="col-md-12">
                            <FormGroup id="inputDescription" label="Description: ">
                                <input id="inputDescription" type="text"
                                    className="form-control"
                                    name="description"
                                    value={this.state.description}
                                    onChange={this.handleChange} />
                            </FormGroup>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-md-6">
                            <FormGroup id="inputYear" label="Year: ">
                                <input id="inputYear" type="number"
                                    className="form-control"
                                    name="year"
                                    value={this.state.year}
                                    onChange={this.handleChange} />
                            </FormGroup>
                        </div>
                        <div className="col-md-6">
                            <FormGroup id="inputMonth" label="Month: ">
                                <SelectMenu id="inputMonth" list={months}
                                    className="form-control"
                                    name="month"
                                    value={this.state.month}
                                    onChange={this.handleChange} />
                            </FormGroup>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-md-4">
                            <FormGroup id="inputValue" label="Value: ">
                                <input id="inputValue" type="number"
                                    className="form-control"
                                    name="value"
                                    value={this.state.value}
                                    onChange={this.handleChange} />
                            </FormGroup>
                        </div>
                        <div className="col-md-4">
                            <FormGroup id="inputType" label="Type: ">
                                <SelectMenu id="inputType" list={types}
                                    className="form-control"
                                    name="type"
                                    value={this.state.type}
                                    onChange={this.handleChange} />
                            </FormGroup>
                        </div>
                        <div className="col-md-4">
                            <FormGroup id="inputStatus" label="Status: ">
                                <input id="inputStatus" type="text"
                                    className="form-control"
                                    name="status"
                                    value={this.state.status}
                                    disabled />
                            </FormGroup>
                        </div>
                    </div>

                    {this.state.updating ?
                        (   
                            <button onClick={this.update} 
                                    className="btn btn-outline-success">
                                    <i className="pi pi-refresh"></i> Update
                            </button>
                            
                        ) :
                        (
                            <button onClick={this.submit} 
                                    className="btn btn-outline-success">
                                    <i className="pi pi-save"></i> Save
                            </button>
                        )
                    }
                    <button onClick={e => this.props.history.push('/search-entries')} 
                            className="btn btn-outline-danger">
                            <i className="pi pi-times"></i>Cancel
                            </button>

                </Card>

            </div>
        )
    }
}

export default withRouter(EntriesRegistration)