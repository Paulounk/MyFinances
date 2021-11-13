import axios from 'axios'

const baseURL = process.env.REACT_APP_API_URL
console.log(baseURL)

const httpClient = axios.create({
    baseURL: baseURL
})

class ApiService {
    constructor(apiurl){
        this.apiurl = apiurl;
    }

    static registerToken(token){
        if(token){
            httpClient.defaults.headers.common['Authorization'] = `Bearer ${token}`
        }
    }

    get(url){
        const requestUrl = `${this.apiurl}${url}`
        return httpClient.get(requestUrl)
    }

    post(url, objeto){
        const requestUrl = `${this.apiurl}${url}`
        return httpClient.post(requestUrl, objeto)
    }

    put(url, objeto){
        const requestUrl = `${this.apiurl}${url}`
        return httpClient.put(requestUrl, objeto)
    }

    delete(url){
        const requestUrl = `${this.apiurl}/${url}`
        return httpClient.delete(requestUrl)
    }
}

export default ApiService