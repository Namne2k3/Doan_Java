import React, { useEffect, useState } from 'react'
import axios from 'axios'
import "./ListProduct.css"
import { ToastContainer, toast } from 'react-toastify'
import NotFound from '../../../../components/NotFound/NotFound'
const ListProduct = () => {

    const BASE_URL = "http://localhost:8080"

    // const [category, setCategory] = useState("Watch")
    // const [category, setCategory] = useState("Mobile Phone")
    const [category, setCategory] = useState("Laptop")
    const [shouldFetch, setShouldFetch] = useState(true);
    const [products, setProducts] = useState([])

    const removeProduct = async (e, id) => {
        await new Promise(resolve => setTimeout(resolve, 1500));
        // alert(`Deleted >>> ${id}`)
        const response = await axios.delete(`${BASE_URL}/api/v1/products/${id}`)
        if (response.data.statusCode === 200) {
            // toast(response.data.message);
            await fetchAllProductsByCategory()
            // setProducts(prevProducts => prevProducts.filter(product => product.id !== id));
        } else if (response.data.statusCode === 404) {
            toast(response.data.message)
        } else {
            toast(response.data.message)
        }

    }

    const fetchAllProductsByCategory = async () => {
        await new Promise(resolve => setTimeout(resolve, 1000));
        const response = await axios.get(`${BASE_URL}/api/v1/products?category=${category}`)
        if (response.data.statusCode === 200) {
            setProducts(prev => response.data.dataList || [])
            // console.log("Check products >>> ", products);
        } else if (response.data.statusCode = 404) {
            setProducts(prev => response.data.dataList || [])
            console.log(response.data.message)
        } else {
            console.log(response.data.message)
        }

    }
    useEffect(() => {
        toast.promise(
            fetchAllProductsByCategory(),
            {
                pending: 'Loading Data Products',
                success: 'Loaded All Products Successfully 👌',
                error: 'Error rejected loading product 🤯'
            },
            { containerId: 'A' }
        )
        // fetchAllProductsByCategory();
    }, [])
    // useEffect(() => {
    //     if (shouldFetch) {
    //         toast.promise(
    //             fetchAllProductsByCategory(),
    //             {
    //                 pending: 'Loading Data Products',
    //                 success: 'Loaded All Products Successfully 👌',
    //                 error: 'Error rejected loading product 🤯'
    //             },
    //             { containerId: 'A' }
    //         );
    //         setShouldFetch(false); // Reset lại state này sau khi fetch dữ liệu
    //     }
    // }, [shouldFetch]); // Chỉ chạy khi `shouldFetch` thay đổi

    // // Effect để cập nhật việc fetch dữ liệu khi `category` thay đổi
    // useEffect(() => {
    //     setShouldFetch(true);
    // }, [category]);

    return (
        <>
            <div className='list add flex-col'>
                <p>All {category}s</p>
                <div className="list-table">
                    <div className="list-table-format title">
                        <b>Image</b>
                        <b>Name</b>
                        <b>Category</b>
                        <b>Price</b>
                        <b>Action</b>
                    </div>
                    {
                        products.length !== 0
                            ?
                            products.map((item, index) => {
                                return (
                                    <a href={`/admin/products/${item.id}`} className="list-table-format" key={index}>
                                        <img src={item.image} alt="product_image" />
                                        <p>{item.name}</p>
                                        <p>{item.category.name}</p>
                                        <p>{item.price}</p>

                                        <p className='cursor'
                                            onClick={(e) => {
                                                e.preventDefault()
                                                if (window.confirm("Delete this product?")) {
                                                    toast.promise(
                                                        removeProduct(e, item.id),
                                                        {
                                                            pending: 'Remove Product is pending',
                                                            success: 'Removed Product Successfully 👌',
                                                            error: 'Error rejected Remove product 🤯'
                                                        },
                                                        { containerId: 'B' }
                                                    )
                                                }
                                            }} onContextMenu={(e) => e.preventDefault()}>X</p>
                                    </a>
                                )
                            })
                            :
                            <NotFound />
                    }
                </div>
            </div>
            <ToastContainer position="top-center" containerId="A" stacked draggable hideProgressBar />
            <ToastContainer containerId="B" stacked draggable hideProgressBar />
        </>
    )
}

export default ListProduct