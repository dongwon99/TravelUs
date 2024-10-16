# Nginx 이미지를 기반으로 시작
FROM nginx:latest

# Nginx 설정 파일 복사 (nginx.conf 대신 default.conf 사용)
COPY default.conf /etc/nginx/conf.d/default.conf

# React 빌드 결과물을 Nginx의 기본 HTML 위치로 복사
COPY build/ /usr/share/nginx/html

# Nginx 포트 설정
EXPOSE 80

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]