import React from "react";

function EntriesTable(props) {

    const rows = props.entries.map( entry =>{
        return (
            <tr key={entry.id}>
                <td>{entry.description}</td>
                <td>{entry.value}</td>
                <td>{entry.type}</td>
                <td>{entry.month}</td>
                <td>{entry.status}</td>
                <td>

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