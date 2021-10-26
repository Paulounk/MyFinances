import ApiService from '../apiservice';

export default class EntryService extends ApiService{

    constructor(){
        super('/entries')
    }

    search(entryFilter){
        
        let params = `?year${entryFilter.year}`

        if(entryFilter.month){
            params = `${params}&month=${entryFilter.month}`
        }

        if(entryFilter.type){
            params = `${params}&type=${entryFilter.type}`
        }

        if(entryFilter.status){
            params = `${params}&status=${entryFilter.status}`
        }

        return this.get(params);
    }
}