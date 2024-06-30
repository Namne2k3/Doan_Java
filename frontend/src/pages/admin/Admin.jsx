import React from 'react'
import { ToastContainer, toast } from 'react-toastify';
import { Outlet } from 'react-router-dom';

import 'react-toastify/dist/ReactToastify.css';

import "./Admin.css"
import SideBar from '../../components/SideBar/SideBar';
const Admin = () => {

    const notify = () => toast("Wow so easy !");

    return (
        <div>

            <div className='admin-content'>
                <SideBar />
                <Outlet />
            </div>
        </div>
    )
}

export default Admin