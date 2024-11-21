aws dynamodb create-table --cli-input-json file://order_table_schema.json --endpoint-url http://localhost:8000 --region eu-west-1

aws dynamodb create-table --cli-input-json file://sale_table_schema.json --endpoint-url http://localhost:8000 --region eu-west-1

aws dynamodb create-table --cli-input-json file://metadata_table_schema.json --endpoint-url http://localhost:8000 --region eu-west-1

aws dynamodb put-item --table-name metadata --item '{"metadata_id": { "S": "Order" }, "increment_id": { "N": "4" }}' \
      --endpoint-url http://localhost:8000 --region eu-west-1

aws dynamodb put-item --table-name metadata --item '{"metadata_id": { "S": "Sale" }, "increment_id": { "N": "4" }}' \
      --endpoint-url http://localhost:8000 --region eu-west-1