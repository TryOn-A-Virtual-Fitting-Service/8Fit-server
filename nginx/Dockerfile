FROM nginx:latest

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
ENTRYPOINT ["nginx"]
CMD ["-g", "daemon off;"]