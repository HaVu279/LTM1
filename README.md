# LTM1
Xây dựng chương trình chia sẻ file ngang hàng giữa các máy tính người dùng (end host) sử dụng máy chủ trung gian (index server). 
Index server sẽ lưu trữ thông tin về danh sách file lưu trữ và địa chỉ của các end hosts. Các bước thực hiện bao gồm: 
  - Các end hosts gửi danh sách file trong thư mục chia sẻ của nó lên index server 
   - Khi một end host A muốn tải file từ index server về  
      • End host A gửi tên file muốn download về index server, index server gửi lại danh sách end hosts có lưu trữ file 
      • End host A gửi yêu cầu tải file đến các end host chứa file 
      • Các end host chứa file gửi file cần download về end host A. Số lượng end host gửi file có thể là 1 hoặc nhiều hơn.
Trong trường hợp nhiều end host cùng gửi, end host A sẽ tải dữ liệu đồng thời. 
          • Sau khi end host A tải file về, end host A sẽ cập nhật lại danh sách file lưu trữ lên server
Yêu cầu chính:  
    I. Khi end host gửi danh sách file trong thư mục chia sẻ của nó lên index server, index server sẽ hiển thị danh sách các file lưu trữ tại end host đó 
    II. Khi end host A gửi tên file muốn download về index server, index server gửi lại danh sách end hosts có lưu trữ file. End host A sẽ hiển thị danh sách end hosts chứa file 
    III. Khi End host A gửi yêu cầu tải file đến các end host chứa file, file tải về đúng và đủ 
    IV. Sau khi end host A tải file về, end host A sẽ cập nhật lại danh sách file lưu trữ lên server 
    V. End host A cho phép tải file đồng thời từ nhiều end host cùng một lúc. Chương trình hiển thị thời gian từ lúc gửi yêu cầu tìm file đến khi tải xong file
......................................................................................................................................
