# API

## Entities

### Stock

1. Id - unique identifier
2. Name - name shown to user
3. Type - category of stock (Bond, Share, etc.)
4. Price - current price

### PriceRecord

1. Id - unique identifier
2. StockId - stock which price is represented
3. Price - current price
4. Timestamp - date and time 

## Endpoints

1. Find all stocks with pagination
2. Find all price records by StockId