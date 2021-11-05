import React from "react";
import Currency from "currency-formatter";

function EntriesTable(props) {

    const rows = props.entries.map( entry =>{
        return (
            <tr key={entry.id}>
                <td>{entry.description}</td>
                <td>{ Currency.format(entry.value, { locale: 'pt-BR' }) }</td>
                <td>{entry.type}</td>
                <td>{entry.month}</td>
                <td>{entry.year}</td>
                <td>{entry.status}</td>
                <td>
                    <button type="button" title="Confirm"
                        disabled={ entry.status !== "PENDING"}
                        className="btn btn-outline-success" 
                        onClick={e => props.updateStatus(entry, 'CONFIRMED')}>
                            <i className="pi pi-check"></i>
                    </button>
                    <button type="button" title="Cancel"
                        disabled={ entry.status !== "PENDING"}
                        className="btn btn-outline-warning" 
                        onClick={e => props.updateStatus(entry, 'CANCELED')}>
                            <i className="pi pi-times"></i>
                    </button>
                    <button type="button" title="Edit"
                        className="btn btn-outline-primary"
                        onClick={e => props.editAction(entry.id)}>
                             <i className="pi pi-pencil"></i>
                    </button>
                    <button type="button" title="Delete"
                        className="btn btn-outline-danger"
                        onClick={e => props.deleteAction(entry)}>
                             <i className="pi pi-trash"></i>
                    </button>
                </td>
            </tr>
        )
    })

    return (
        <table className="table table-hover">
            <thead>
                <tr>
                    <th scope="col">Description</th>
                    <th scope="col">Value</th>
                    <th scope="col">Type</th>
                    <th scope="col">Month</th>
                    <th scope="col">Year</th>
                    <th scope="col">Situation</th>
                    <th scope="col">Actions</th>
                </tr>
            </thead>
            <tbody>
                {rows}
            </tbody>
        </table>
    )
}

export default EntriesTable