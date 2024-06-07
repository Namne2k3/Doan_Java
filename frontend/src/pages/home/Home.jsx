import React, { useEffect, useState } from 'react'
import "./home.css"
import Header from '../../components/Header/Header'
import Menu from '../../components/Menu/Menu'
import TechDisplay from '../../components/TechDisplay/TechDisplay'
import { fetchALlCategories } from '../../services/CategoryService'


const Home = () => {

    const [category, setCategory] = useState("")
    const [menuList, setMenuList] = useState([])

    useEffect(async () => {

        const dataCategories = await fetchALlCategories();
        if (dataCategories) {
            setMenuList(dataCategories);
        }
    }, [])


    return (
        <div>
            <Header />
            <Menu menuList={menuList} category={category} setCategory={setCategory} />
            <TechDisplay category={category} />
        </div>
    )
}

export default Home