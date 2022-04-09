import React from 'react';
import ReactDOM from 'react-dom';
import AddRecord from './AddRecord';
import FetchRecord from './FetchRecord';


ReactDOM.render(
  <React.StrictMode>
    
	<AddRecord />
	<FetchRecord />
  </React.StrictMode>,
  document.getElementById('root')
);

