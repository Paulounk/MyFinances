import React from 'react';
import Routes from './routes';
import Navbar from '../components/navbar'
import AuthProvider from './authProvider';

import 'toastr/build/toastr.min.js'
import 'bootswatch/dist/flatly/bootstrap.css';
import '../custom.css'
import 'toastr/build/toastr.css'

import 'primereact/resources/themes/saga-blue/theme.css'
import 'primereact/resources/primereact.min.css'
import 'primeicons/primeicons.css'

class App extends React.Component {

  render() {
    return (
      <>
        <AuthProvider>
          <Navbar />
            <Routes />
        </AuthProvider>
      </>
    )
  }
}

export default App;
