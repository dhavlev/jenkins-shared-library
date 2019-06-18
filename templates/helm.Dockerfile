FROM alpine:edge

LABEL maintainer="visharad.dhavle@gmail.com"

ENV HELM_VERSION="2.11.0"
ENV KUBE_VERSION="v1.11.3"
ENV KUBE_BASE_URL="https://storage.googleapis.com/kubernetes-release/release/${KUBE_VERSION}/bin/linux/amd64/kubectl"
ENV HELM_BASE_URL="https://storage.googleapis.com/kubernetes-helm"
ENV HELM_TAR_FILE="helm-v${HELM_VERSION}-linux-amd64.tar.gz"
ENV AWS_VERSION="1.14.5"

RUN apk add -v --update --no-cache curl python groff less mailcap && \
    apk add -v --update --no-cache -t deps ca-certificates py-pip && \
    curl -L ${HELM_BASE_URL}/${HELM_TAR_FILE} |tar xvz && \
    mv linux-amd64/helm /usr/bin/helm && \
    chmod +x /usr/bin/helm && \
    rm -rf linux-amd64 && \
    curl -L ${KUBE_BASE_URL} -o /usr/local/bin/kubectl && \
    chmod +x /usr/local/bin/kubectl && \
    pip install --upgrade awscli==${AWS_VERSION} && \
    apk del --purge deps && \
    curl -o aws-iam-authenticator https://amazon-eks.s3-us-west-2.amazonaws.com/1.12.7/2019-03-27/bin/linux/amd64/aws-iam-authenticator && \
    chmod +x ./aws-iam-authenticator && \
    mkdir -p $HOME/bin && cp ./aws-iam-authenticator $HOME/bin/aws-iam-authenticator && export PATH=$HOME/bin:$PATH && \
    echo 'export PATH=$HOME/bin:$PATH' >> ~/.bashrc && \
    helm init --client-only && \
    helm repo update

WORKDIR /root
ENTRYPOINT ["kubectl"]
CMD ["help"]