# Desafio Backend Itaú

## instalação AWS CLI

    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64-2.0.30.zip" -o "awscliv2.zip"
    
    unzip awscliv2.zip
    
    sudo ./aws/install

## Testes

    export AWS_DEFAULT_REGION=sa-east-1
    export AWS_ACCESS_KEY_ID=test
    export AWS_SECRET_ACCESS_KEY=test
    aws --endpoint-url=http://localhost:4566 --region sa-east-1 sqs receive-message --queue-url http://localhost:4566/000000000000/transacoes-financeiras-processadas --max-number-of-messages 10