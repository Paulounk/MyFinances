import React from 'react';
import { withRouter } from 'react-router-dom';

import Card from '../../components/card';
import FormGroup from '../../components/form-group';
import SelectMenu from '../../components/select-menu';
import EntriesTable from './entries-table'
import EntryService from '../../app/service/entryService';
import LocalStorageService from '../../app/service/localstorageService';

import * as messages from '../../components/toastr';

import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button'

class SearchEntries extends React.Component {

    state = {
        year: '',
        month: '',
        type: '',
        description: '',
        entries: [],
        showConfirmDialog: false,
        entryDelete: []
    }

    constructor() {
        super();
        this.service = new EntryService();
    }

    search = () => {

        if (!this.state.year) {
            messages.messageAlert('The year fild is required!')
            return false
        }
        const userAcess = LocalStorageService.getItem('_user_acess')

        const entryFilter = {
            year: this.state.year,
            month: this.state.month,
            type: this.state.type,
            description: this.state.description,
            user: userAcess.id
        }

        this.service
            .search(entryFilter)
            .then(response => {
                const list = response.data
                if(list.length < 1){
                    messages.messageAlert("No results found!")
                }
                this.setState({ entries: response.data })
            }).catch(error => {
                console.log(error)
            })
    }

    edit = (id) => {
        this.props.history.push(`/entries-registration/${id}`)
    }

    openConfirmation = (entry) => {
        this.setState({ showConfirmDialog: true, entryDelete: entry })
    }

    cancelDelete = () => {
        this.setState({ showConfirmDialog: false, entryDelete: {} })
    }

    delete = () => {
        this.service
            .delete(this.state.entryDelete.id)
            .then(response => {
                const entries = this.state.entries;
                const index = entries.indexOf(this.state.entryDelete);
                entries.splice(index, 1);
                this.setState({ entries: entries, showConfirmDialog: false })

                messages.messageSuccess('Successfully deleted release!')
            }).catch(error => {
                messages.messageError("An error occorred while trying to delete release.")
            })
    }

    redirectFormRegistration = () => {
        this.props.history.push('/entries-registration')
    }

    updateStatus = (entry, status) => {
        this.service
            .updateStatus(entry.id, status)
            .then(response => {
                const entries = this.state.entries;
                const index = entries.indexOf(entry)
                if (index !== -1) {
                    entry['status'] = status;
                    entries[index] = entry
                    this.setState({ entries })
                }
                messages.messageSuccess("Status updated successfully!")
            })
    }

    render() {
        const months = this.service.getListMonth();
        const types = this.service.getListTypes();

        const confirmDialogFooter = (
            <div>
                <Button label="Confirm" icon="pi pi-check" onClick={this.delete} className="p-button-success" />
                <Button label="Cancel" icon="pi pi-times" onClick={this.cancelDelete} className="p-button-danger" />
            </div>
        );

        return (
            <div className="container">
                <Card title="Search Entries">
                    <div className="row">
                        <div className="col-md-6">
                            <div className="bs-component">

                                <FormGroup htmlFor="inputYear" label="Year *">
                                    <input type="number"
                                        className="form-control"
                                        id="inputYear"
                                        value={this.state.year}
                                        onChange={e => this.setState({ year: e.target.value })}
                                        placeholder="Enter a Year" />
                                </FormGroup>

                                <FormGroup htmlFor="inputMonth" label="Month">
                                    <SelectMenu id="inputMonth"
                                        value={this.state.month}
                                        onChange={e => this.setState({ month: e.target.value })}
                                        className="form-control"
                                        list={months} />
                                </FormGroup>

                                <FormGroup htmlFor="inputType" label="Type">
                                    <SelectMenu id="inputType"
                                        value={this.state.type}
                                        onChange={e => this.setState({ type: e.target.value })}
                                        className="form-control"
                                        list={types} />
                                </FormGroup>

                                <FormGroup htmlFor="inputDescription" label="Description *">
                                    <input type="text"
                                        className="form-control"
                                        id="inputDescription"
                                        value={this.state.description}
                                        onChange={e => this.setState({ description: e.target.value })}
                                        placeholder="Enter a description" />
                                </FormGroup>

                                <button onClick={this.search}
                                    type="button"
                                    className="btn btn-outline-success">
                                    <i className="pi pi-search"></i> Search
                                </button>
                                <button onClick={this.redirectFormRegistration}
                                    type="button"
                                    className="btn btn-outline-info">
                                    <i className="pi pi-plus"></i> Register
                                </button>

                            </div>
                        </div>
                    </div>

                    <div className="row">
                        <div className="col-md-12">
                            <div className="bs-component mt-4">
                                <EntriesTable entries={this.state.entries}
                                    deleteAction={this.openConfirmation}
                                    editAction={this.edit}
                                    updateStatus={this.updateStatus} />
                            </div>
                        </div>
                    </div>
                    <div>
                        <Dialog header="Confirm deletion?"
                            visible={this.state.showConfirmDialog}
                            style={{ width: '50vw' }}
                            footer={confirmDialogFooter}
                            modal={true}
                            onHide={() => this.setState({ showConfirmDialog: false })}>
                            <p>Do you confirm the deletion of this release?</p>
                        </Dialog>
                    </div>
                </Card>
            </div>
        )
    }
}

export default withRouter(SearchEntries);