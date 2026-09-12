import {useState} from 'react';
import Sidebar from './components/Sidebar';
import SchoolsPage from './pages/SchoolsPage';
import StudentsPage from './pages/StudentsPage';

export default function AdminDashboard() {
   const [activeView, setActiveView] = useState('schools');

   return (
       <div className="flex h-screen bg-slate-50">
          <Sidebar activeView={activeView} onNavigate={setActiveView}/>
          <main className="flex-1 overflow-auto p-8">
             {activeView === 'schools' && <SchoolsPage/>}
             {activeView === 'students' && <StudentsPage/>}
             {activeView === 'staff' && (
                 <div className="flex items-center justify-center h-full text-slate-500">Staff view coming soon</div>
             )}
             {activeView === 'settings' && (
                 <div className="flex items-center justify-center h-full text-slate-500">Settings view coming
                    soon</div>
             )}
          </main>
       </div>
   );
}