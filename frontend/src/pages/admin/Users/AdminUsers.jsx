import React, { useContext, useEffect, useState } from 'react'
import "./AdminUsers.css"
import NotFound from '../../../components/NotFound/NotFound'
import { toast, ToastContainer } from 'react-toastify'
import axios from 'axios'
import { StoreContext } from "../../../context/StoreContext"
import Popup from 'reactjs-popup';
import { useLocation } from 'react-router-dom'
import 'reactjs-popup/dist/index.css';
import UserInformation from '../../../components/UserInformation/UserInformation'
import ReactPaginate from 'react-paginate'
const AdminUsers = () => {

    const token = localStorage.getItem('token')
    const [users, setUsers] = useState([])
    const { profileInfo } = useContext(StoreContext)
    const [pageCount, setPageCount] = useState('1')
    const [search, setSearch] = useState("")

    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const currentPage = parseInt(queryParams.get('page')) || 1;

    useEffect(() => {
        const fetchAllUsers = async () => {
            try {
                const res = await axios.get(`http://localhost:8080/admin/get-all-users?page=${currentPage}`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                })

                if (res.data.statusCode === 200) {
                    setUsers(res.data.dataList)
                    setPageCount(prev => Math.ceil(res.data.amountAllData / 10))
                } else {
                    throw new Error(res.data.message)
                }
            } catch (e) {
                toast.error(e.message)
            }
        }

        fetchAllUsers()
    }, [])

    const handleSearchUsers = async (e) => {
        e.preventDefault()
        try {
            const res = await axios.get(`http://localhost:8080/admin/users/search?search=${search.toLowerCase().trim()}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })
            if (res.data.statusCode === 200) {
                setUsers(res.data.dataList)
                setPageCount(prev => Math.ceil(res.data.amountAllData / 10))
            } else {
                throw new Error(res.data.message)
            }
        } catch (e) {
            toast.error(e.message)
        }
    }

    const handleLockUser = async (id, isLock) => {
        try {
            const res = await axios.put(`http://localhost:8080/admin/users/update/${id}?lock=${isLock}`, {}, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })
            if (res.data.statusCode === 200) {
                toast.success(res.data.message)
            } else {
                throw new Error(res.data.message)
            }
        } catch (e) {
            toast.error(e.message)
        }
    }

    const handleCloseAfterDelete = (close, id) => {
        close()
        var element = document.getElementById(`list-table-users-format_${id}`)
        if (element) {
            alert("Đã xóa tài khoản")
            element.remove()
        }
    }

    return (
        <>
            <div className='list add flex-col'>
                <form onSubmit={handleSearchUsers} className='user_search_form'>
                    {/* <label htmlFor="name">Tìm kiếm</label> */}
                    <input value={search} onChange={(e) => setSearch(prev => e.target.value)} id='search' name='search' type="text" placeholder='Tên, email hoặc SĐT' />
                    <button type='submit'>Tìm</button>
                </form>
                <div className="list-table-users">
                    <div className="list-table-users-format title">
                        <b>Hình đại diện</b>
                        <b>Tên tài khoản</b>
                        <b>Email</b>
                        <b>Số điện thoại</b>
                        <b>Địa chỉ</b>
                        <b></b>
                    </div>
                    {
                        users?.length > 0
                            ?
                            users?.map((user, index) => {

                                return (
                                    <div id={`list-table-users-format_${user?.id}`} className="list-table-users-format" key={index}>
                                        <img src={`/images/${user.image}`} alt="profile_image" />
                                        <p>{user.username}</p>
                                        <p>{user.email}</p>
                                        <p>{user.phone}</p>
                                        <p>{user.address}</p>


                                        <div className='action_container'>
                                            <Popup
                                                trigger={<button>Chi tiết</button>}
                                                modal
                                                nested
                                            >
                                                {close => <UserInformation handleLockUser={handleLockUser} user={user} close={close} handleCloseAfterDelete={handleCloseAfterDelete} />}
                                            </Popup>
                                            {
                                                user?.enabled === true ?
                                                    profileInfo?.id === user?.id || user?.role === "ADMIN" ?
                                                        <button disabled style={{ opacity: "0.7" }}>
                                                            Khóa
                                                        </button>
                                                        :
                                                        <button onClick={() => {
                                                            if (window.confirm("Bạn có chắc chắn muốn khóa tài khoản này?")) {
                                                                handleLockUser(user?.id, true)
                                                            }
                                                        }}>
                                                            Khóa
                                                        </button>
                                                    :
                                                    <button onClick={() => {
                                                        if (window.confirm("Bạn có chắc chắn muốn mở khóa tài khoản này?")) {
                                                            handleLockUser(user?.id, false)
                                                        }
                                                    }}>
                                                        Mở khóa
                                                    </button>

                                            }
                                        </div>
                                    </div>
                                )
                            })
                            :
                            <NotFound />
                    }
                </div>
                {
                    pageCount !== null &&
                    <ReactPaginate
                        className='pagination_order'
                        breakLabel="..."
                        nextLabel=">>"
                        onPageChange={(e) => window.location.href = `/admin/users?page=${e.selected + 1}`}
                        pageRangeDisplayed={5}
                        pageCount={pageCount}
                        previousLabel="<<"
                        renderOnZeroPageCount={null}
                        forcePage={currentPage - 1}
                    />
                }
            </div >
            <ToastContainer draggable stacked autoClose={2000} hideProgressBar />
        </>
    )
}

export default AdminUsers