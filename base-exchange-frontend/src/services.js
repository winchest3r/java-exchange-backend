import axios from 'axios';

const currenciesBaseUrl = '/api/currencies';

const getCurrencies = async () => {
    const response = await axios.get(currenciesBaseUrl);
    return response.data;
};

export default {
    getCurrencies,
};