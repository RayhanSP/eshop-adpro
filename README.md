# Tutorial Modul Pemrograman Lanjut 2024/2025

## Reflection 1 
Saya berusaha mengikuti prinsip clean code dengan memastikan nama variabel dan fungsi yang saya gunakan bermakna, sehingga kode bisa self-describing dan tidak memerlukan komentar tambahan. Selain itu, untuk menghindari null-exception, saya memvalidasi input, seperti yang saya terapkan pada fitur edit dan delete produk. Saya juga berusaha meningkatkan keamanan dengan memastikan penggunaan metode POST pada fitur delete, untuk mengurangi potensi celah. Fokus utama saya adalah menjaga kode tetap jelas dan aman, serta menghindari masalah yang bisa terjadi karena input yang tidak valid.

## Reflection 2
Setelah menulis unit test, saya merasa lebih yakin bahwa fungsionalitas utama aplikasi sudah diuji dengan baik. Jumlah unit test dalam satu kelas sebaiknya disesuaikan dengan kompleksitas fungsionalitas yang diuji. 100% code coverage menunjukkan bahwa semua baris kode sudah diuji, tetapi bukan jaminan bahwa kode bebas bug.

Jika diminta membuat suite uji baru untuk memverifikasi jumlah item dalam daftar produk, kita bisa menghadapi masalah kebersihan kode jika mengulang prosedur setup dan variabel yang sama. Ini bisa menambah duplikasi dan mengurangi kualitas kode. Solusinya adalah dengan membuat metode setup bersama yang dapat digunakan di beberapa test case untuk mengurangi duplikasi dan menjaga kode tetap bersih dan terorganisir.

