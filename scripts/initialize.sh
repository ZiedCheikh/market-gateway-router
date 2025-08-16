aws dynamodb create-table --cli-input-json file://order_table_schema.json --endpoint-url http://localhost:4566 --region eu-west-1

aws dynamodb create-table --cli-input-json file://sale_table_schema.json --endpoint-url http://localhost:4566 --region eu-west-1

aws dynamodb create-table --cli-input-json file://metadata_table_schema.json --endpoint-url http://localhost:4566 --region eu-west-1

aws dynamodb put-item --table-name metadata --item '{"metadata_id": { "S": "Order" }, "increment_id": { "N": "4" }}' \
      --endpoint-url http://localhost:4566 --region eu-west-1

aws dynamodb put-item --table-name metadata --item '{"metadata_id": { "S": "Sale" }, "increment_id": { "N": "4" }}' \
      --endpoint-url http://localhost:4566 --region eu-west-1

aws --endpoint-url=http://localhost:4566 s3api create-bucket \
  --bucket sales \
  --region eu-west-1 \
  --create-bucket-configuration LocationConstraint=eu-west-1

aws --endpoint-url=http://localhost:4566 s3api put-bucket-cors \
  --bucket sales \
  --region eu-west-1 \
  --cors-configuration file://cors.json
