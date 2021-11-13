import ApiService from '../apiservice';

import ErrorValidation from '../exception/ErrorValidation';

export default class EntryService extends ApiService {

    constructor() {
        super('/entries')
    }

    getListMonth() {
        return [
            { label: 'Select', value: '' },
            { label: 'January', value: '1' },
            { label: 'February', value: '2' },
            { label: 'March', value: '3' },
            { label: 'April', value: '4' },
            { label: 'May', value: '5' },
            { label: 'June', value: '6' },
            { label: 'July', value: '7' },
            { label: 'August', value: '8' },
            { label: 'September', value: '9' },
            { label: 'October', value: '10' },
            { label: 'November', value: '11' },
            { label: 'December', value: '12' },
        ]
    }

    getListTypes(){
        return [
            {label: 'Select', value:''},
            {label: 'Revenue', value:'REVENUE'},
            {label: 'Expenditure', value:'EXPENDITURE'}
        ]
    }

    getEntryById(id){
        return this.get(`/${id}`);
    }

    updateStatus(id, status){
        return this.put(`/${id}/update-status`, { status })
    }

    validate(entry){
        const erros = [];

        if(!entry.year){
            erros.push("Enter a year.")
        }

        if(!entry.month){
            erros.push("Enter a month.")
        }

        if(!entry.description){
            erros.push("Enter a description.")
        }

        if(!entry.value){
            erros.push("Enter a value.")
        }

        if(!entry.type){
            erros.push("Enter a type.")
        }

        if(erros && erros.length > 0){
            throw new ErrorValidation(erros)
        }

    }

    save(entry){
        return this.post('/', entry)
    }

    update(entry){
        return this.put(`/${entry.id}`, entry);
    }

    search(entryFilter) {

        let params = `?year=${entryFilter.year}`

        if (entryFilter.month) {
            params = `${params}&month=${entryFilter.month}`
        }

        if (entryFilter.type) {
            params = `${params}&type=${entryFilter.type}`
        }

        if (entryFilter.status) {
            params = `${params}&status=${entryFilter.status}`
        }

        if (entryFilter.user) {
            params = `${params}&user=${entryFilter.user}`
        }

        if(entryFilter.description){
            params = `${params}&description=${entryFilter.description}`
        }

        return this.get(params);
    }

    remove(id){
        return this.delete(`/${id}`)
    }
}