import axios from "axios";

const BASE_URL = "http://localhost:8080"

const login = async (email, password) => {
    try {
        const res = await axios.post(`${BASE_URL}/auth/login`, {
            email,
            password
        })

        const data = await res.data;

        return data;


    } catch (err) {
        throw err;
    }
}

const register = async (userData, token) => {
    try {

        const res = await axios.post(`${BASE_URL}/auth/register`, userData, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        })
        const data = await res.data;
        console.log("Check data register >>> ", data);
        return data;

    } catch (error) {
        throw error;
    }
}

const getAllUser = async (token) => {
    try {

        const res = await axios.get(`${BASE_URL}/admin/get-all-users`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        })

        return res.data;

    } catch (error) {
        throw error;
    }
}


const getUserProfile = async (token) => {
    try {

        const res = await axios.get(`${BASE_URL}/adminuser/get-profile`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        })

        return res.data;
    } catch (err) {
        throw err;
    }
}

const getUserById = async (userId, token) => {
    try {
        const response = await axios.get(`${BASE_URL}/admin/get-users/${userId}`,
            {
                headers: { Authorization: `Bearer ${token}` }
            })
        return response.data;
    } catch (err) {
        throw err;
    }
}

const deleteUser = async (userId, token) => {
    try {
        const response = await axios.delete(`${BASE_URL}/admin/delete/${userId}`, {
            headers: { Authorization: `Bearer ${token}` }
        })
        return response.data;
    } catch (err) {
        throw err;
    }
}

const updateUser = async (userId, userData, token) => {
    try {
        const response = await axios.put(`${BASE_URL}/admin/update/${userId}`, userData, {
            headers: { Authorization: `Bearer ${token}` }
        })
        return response.data;
    } catch (err) {
        throw err;
    }
}

/**AUTHENTICATION CHECKER */

function logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('role')
}

function isAuthenticated() {
    const token = localStorage.getItem('token')
    return !!token;
}

function isAdmin() {
    const role = localStorage.getItem('role')
    return role === 'ADMIN'
}


function isUser() {
    const role = localStorage.getItem('role')
    return role === 'USER'
}

function adminOnly() {

    // console.log("Check authenticated >>> ", isAuthenticated());
    // console.log("Check isAdmin >>> ", isAdmin());

    return isAuthenticated() && isAdmin();
}

export {
    login,
    register,
    getAllUser,
    getUserProfile,
    getUserById,
    deleteUser,
    updateUser,
    logout,
    isAuthenticated,
    isAdmin,
    isUser,
    adminOnly
}