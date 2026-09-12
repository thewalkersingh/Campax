import 'react';
import {Routes, Route} from 'react-router-dom';
import AdminDashboard from "./dashboard/AdminDashboard.jsx";

function App() {
   return (
       <Routes>
          <Route path="/">
             <Route index element={<AdminDashboard/>}/>
          </Route>
       </Routes>
   );
}

export default App;