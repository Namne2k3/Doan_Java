import React, { useContext, useEffect, useState } from 'react'
import "./techdisplay.css"
import { StoreContext } from '../../context/StoreContext'
import TechItem from '../TechItem/TechItem';
import NotFound from '../NotFound/NotFound';
import { ToastContainer, toast } from 'react-toastify';
import axios from 'axios';

const TechDisplay = ({ category, search = null, brandList }) => {

    const { fetchProductBySearching, products } = useContext(StoreContext);
    const BASE_URL = `http://localhost:8080`
    const [categories, setCategories] = useState([])
    const [populars, setPopulars] = useState([])


    const addCartItem = async (id, name) => {
        toast.promise(
            addToCart(id),
            {
                pending: 'Thêm vào giỏ hàng',
                success: `Đã thêm ${name} vào giỏ hàng 👌`,
                error: 'Có lỗi khi thêm sản phẩm vào giỏ hàng 🤯'
            },
            { containerId: 'C' }
        )
    }

    const { cartItems, addToCart } = useContext(StoreContext);


    useEffect(() => {

        const fetchCategory = async () => {
            try {
                const response = await axios.get(`${BASE_URL}/api/v1/categories`);
                if (response.data.statusCode === 200) {
                    setCategories(response.data.dataList);
                } else {
                    throw new Error(response.data.message)
                }

            } catch (e) {
                console.error(e.message);
            }
        }

        const fetchPopulars = async () => {
            try {
                const response = await axios.get(`${BASE_URL}/api/v1/products/populars`);
                if (response.data.statusCode === 200) {
                    setPopulars(response.data.dataList);
                } else {
                    throw new Error(response.data.message)
                }

            } catch (e) {
                console.error(e.message);
            }
        }

        search &&
            fetchProductBySearching(search)

        fetchCategory()
        fetchPopulars()

    }, [search])

    return (
        <>
            <div className='tech-display' id='tech-display'>
                <h2>Sản phẩm nổi bật</h2>
                {
                    populars?.length !== 0
                        ?
                        <div className="tech-display-list">
                            {
                                populars?.map((item, index) => {
                                    return (
                                        <TechItem addCartItem={addCartItem} cartItems={cartItems} item={item} key={index} />
                                    )
                                })
                            }
                        </div>
                        :
                        <NotFound />
                }
                {
                    categories?.map((cate, index) => {
                        return (
                            <div id={cate.name} key={index} className='mt-5'>
                                <h2>{cate.name}</h2>
                                {
                                    products?.length &&
                                    <div className="tech-display-list">
                                        {
                                            products?.map((item, ProductIndex) => {
                                                if (item.category.id === cate.id) {
                                                    return (
                                                        <TechItem addCartItem={addCartItem} cartItems={cartItems} item={item} key={ProductIndex} />
                                                    )
                                                }
                                            })
                                        }
                                    </div>
                                }
                            </div>
                        )
                    })
                }
                {
                    brandList?.map((brand, brandIndex) => {
                        return (
                            <div id={brand.name} key={brandIndex} className='mt-5'>
                                <h2>Thương hiệu {brand.name}</h2>
                                {
                                    products?.length &&
                                    <div className="tech-display-list">
                                        {
                                            products?.map((item, ProductIndex) => {
                                                if (item.brand.id === brand.id) {
                                                    return (
                                                        <TechItem addCartItem={addCartItem} cartItems={cartItems} item={item} key={ProductIndex} />
                                                    )
                                                }
                                            })
                                        }
                                    </div>
                                }
                            </div>
                        )
                    })
                }
            </div>
            <ToastContainer hideProgressBar draggable stacked autoClose={1500} containerId="C" />
        </>
    )
}

export default TechDisplay