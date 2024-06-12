import React, { useContext, useEffect } from 'react'
import { StoreContext } from '../../../../context/StoreContext'
import axios from 'axios'
import "./ListOrder.css"
const ListOrder = () => {

    const BASE_URL = "http://localhost:8080"


    useEffect(() => {

        const fetchAllOrder = async () => {
            const res = await axios.get(`${BASE_URL}/api/v1/orders`)
            if (res.data) {
                console.log(res.data.dataList);
            }
        }

        fetchAllOrder();

    }, [])
    return (
        <div>ListOrder</div>
    )
}

export default ListOrder