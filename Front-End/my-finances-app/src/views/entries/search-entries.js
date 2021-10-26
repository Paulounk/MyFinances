import React from 'react';
import { withRouter } from 'react-router-dom';
import Card from '../../components/card';
import FormGroup from '../../components/form-group';
import SelectMenu from '../../components/select-menu';
import EntriesTable from './entries-table'

class SearchEntries extends React.Component {

    state = {
        year:'',
        month:'',
        type:''
    }

    search = () => {
        console.log(this.state)
    }

    render() {
        const months = [
            {label: 'Select', value: ''},
            {label: 'January', value: '1'},
            {label: 'February', value: '2'},
            {label: 'March', value: '3'},
            {label: 'April', value: '4'},
            {label: 'May', value: '5'},
            {label: 'June', value: '6'},
            {label: 'July', value: '7'},
            {label: 'August', value: '8'},
            {label: 'September', value: '9'},
            {label: 'October', value: '10'},
            {label: 'November', value: '11'},
            {label: 'December', value: '12'},
        ]

        const types = [
            {label: 'Selecione', value:''},
            {label: 'Revenue', value:'REVENUE'},
            {label: 'Expenditure', value:'EXPENDITURE'}
        ]

        const entries = [
            {id: 1, description: 'Salario', value: 5000, month: 1, type: 'REVENUE', status: 'Confirmed'}
        ]

        return (
            <div className="container">
                <Card title="Search Entries">
                    <div className="row">
                        <div className="col-md-6">
                            <div className="bs-component">

                                <FormGroup htmlFor="inputYear" label="Year *">
                                    <input type="text"
                                        className="form-control"
                                        id="inputYear"
                                        value={this.state.year}
                                        onChange={e => this.setState({year: e.target.value})}
                                        placeholder="Enter a Year"/>
                                </FormGroup>

                                <FormGroup htmlFor="inputMonth" label="Month">
                                    <SelectMenu id="inputMonth" 
                                                value={this.state.month}
                                                onChange={e => this.setState({month: e.target.value})}
                                                className="form-control" 
                                                list={months}/>
                                </FormGroup>

                                <FormGroup htmlFor="inputType" label="Type">
                                    <SelectMenu id="inputType" 
                                                value={this.state.type}
                                                onChange={e => this.setState({type: e.target.value})}
                                                className="form-control" 
                                                list={types}/>
                                </FormGroup>

                                <button onClick={this.search} type="button" className="btn btn-outline-success">Search</button>
                                <button type="button" className="btn btn-outline-info">Register</button>

                            </div>
                        </div>
                    </div>

                    <div className="row">
                        <div className="col-md-12">
                            <div className="bs-component mt-4"> 
                                <EntriesTable entries={entries}/>
                            </div>
                        </div>
                    </div>
                </Card>
            </div>
        )
    }
}

export default withRouter(SearchEntries);