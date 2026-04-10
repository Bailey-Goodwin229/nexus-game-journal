import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../api/axios';

const GameJournalPage = () => {
    const { gameTitle } = useParams(); // Grabs the title from the URl
    const [entries, setEntries] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        try {
            // You can either fetch all
        }
    })
}