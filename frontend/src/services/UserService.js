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
function formatDate(date) {
    let day = date.getDate();
    let month = date.getMonth() + 1; // Tháng được đếm từ 0-11, cần +1 để đúng với tháng 1-12
    let year = date.getFullYear();

    // Thêm số 0 phía trước nếu ngày hoặc tháng chỉ có 1 chữ số
    day = day < 10 ? '0' + day : day;
    month = month < 10 ? '0' + month : month;

    return `${day}-${month}-${year}`;
}
const register = async (userData) => {
    try {

        console.log({ ...userData, createdAt: formatDate(new Date()), updatedAt: formatDate(new Date()), role: "USER" });
        const res = await axios.post(`${BASE_URL}/auth/register`, { ...userData, createdAt: new Date(), updatedAt: new Date(), role: "USER" })
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