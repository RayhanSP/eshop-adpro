# Tutorial Modul Pemrograman Lanjut 2024/2025

## Module 1
<details>
  <summary>Reflection 1</summary>

Saya berusaha mengikuti prinsip clean code dengan memastikan nama variabel dan fungsi yang saya gunakan bermakna, sehingga kode bisa self-describing dan tidak memerlukan komentar tambahan. Selain itu, untuk menghindari null-exception, saya memvalidasi input, seperti yang saya terapkan pada fitur edit dan delete produk. Saya juga berusaha meningkatkan keamanan dengan memastikan penggunaan metode POST pada fitur delete, untuk mengurangi potensi celah. Fokus utama saya adalah menjaga kode tetap jelas dan aman, serta menghindari masalah yang bisa terjadi karena input yang tidak valid.

</details>

<details>
  <summary>Reflection 2</summary>

Setelah menulis unit test, saya merasa lebih yakin bahwa fungsionalitas utama aplikasi sudah diuji dengan baik. Jumlah unit test dalam satu kelas sebaiknya disesuaikan dengan kompleksitas fungsionalitas yang diuji. 100% code coverage menunjukkan bahwa semua baris kode sudah diuji, tetapi bukan jaminan bahwa kode bebas bug.

Jika diminta membuat suite uji baru untuk memverifikasi jumlah item dalam daftar produk, kita bisa menghadapi masalah kebersihan kode jika mengulang prosedur setup dan variabel yang sama. Ini bisa menambah duplikasi dan mengurangi kualitas kode. Solusinya adalah dengan membuat metode setup bersama yang dapat digunakan di beberapa test case untuk mengurangi duplikasi dan menjaga kode tetap bersih dan terorganisir.

</details>

---

## Module 2
<details>
  <summary>Reflection 1</summary>

Beberapa masalah kode yang diperbaiki meliputi kurangnya cakupan unit test, penggunaan Mockito yang salah, serta penanganan nilai null yang kurang baik. Untuk meningkatkan cakupan, saya menambahkan unit test pada cabang kode yang belum teruji berdasarkan laporan JaCoCo. Masalah Mockito (doNothing() pada non-void method) diperbaiki dengan menggantinya dengan when(...).thenReturn(...). Saya juga menambahkan validasi input untuk menangani null agar kode lebih aman. Selain itu, saya membersihkan import yang tidak digunakan dan memperbaiki code smells yang terdeteksi oleh PMD. Strateginya adalah menggunakan alat analisis otomatis, melakukan refactoring, dan menyesuaikan test suite untuk memastikan kualitas kode meningkat.

</details>

<details>
  <summary>Reflection 2</summary>

Implementasi CI/CD ini sudah memenuhi Continuous Integration (CI) dan Continuous Deployment (CD). Setiap push ke branch mana pun akan menjalankan pengujian otomatis, analisis kualitas kode, dan pelaporan cakupan kode. Jika semua tes lolos, kode secara otomatis dideploy ke Koyeb tanpa perlu intervensi manual. Hal ini memastikan bahwa setiap perubahan dapat diuji dan dirilis dengan cepat. Selain itu, pipeline berjalan di semua branch, memungkinkan pengembangan paralel tanpa mengganggu integrasi utama. Meskipun sudah sesuai prinsip CI/CD, masih bisa ditingkatkan dengan menambahkan rollback otomatis jika terjadi kegagalan deployment dan menerapkan strategi blue-green deployment untuk menghindari downtime. 

</details>

---

## Module 3
<details>
  <summary>Reflection 1</summary>

Pada modul ini, saya mengimplementasikan 3 prinsip SOLID :
1. SRP : Saya memisahkan CarController dari ProductController, sehingga masing-masing hanya bertanggung jawab atas satu jenis entitas
2. OCP : Saya mengubah CarRepository menjadi sebuah interface, lalu membuat CarRepositoryImpl sebagai implementasi konkret
3. DIP : CarServiceImpl sekarang bergantung pada interface CarRepository, bukan implementasi langsung (CarRepositoryImpl)

</details>

<details>
  <summary>Reflection 2</summary>

Dengan menerapkan prinsip SOLID, proyek saya menjadi lebih modular dan maintainable. Misalnya, Dengan memisahkan CarController dan ProductController, developer baru dapat langsung memahami fungsi masing-masing tanpa kebingungan. Dengan menerapkan OCP di CarRepository, kita bisa mengganti CarRepositoryImpl dengan penyimpanan lain seperti database, tanpa harus mengubah kode di CarServiceImpl. Dengan DIP, CarServiceImpl bergantung pada interface CarRepository, sehingga bisa diuji dengan mock repository tanpa harus menggunakan implementasi konkret.

</details>

<details>
  <summary>Reflection 3</summary>

Jika prinsip SOLID tidak diterapkan, proyek dapat mengalami beberapa potensi masalah, seperti jika CarController masih menangani Product, maka setiap kali ada perubahan dalam Product, kita juga harus mengecek apakah ada dampaknya terhadap Car. Jika CarServiceImpl langsung menggunakan implementasi CarRepositoryImpl, maka saat testing kita harus selalu menggunakan data asli, tidak bisa menggunakan mock repository. Jika CarServiceImpl masih menggunakan new CarRepository(), maka jika ingin mengganti cara penyimpanan data ke database, kita harus mengubah semua tempat yang menggunakan CarRepository.

</details>

---

## Module 4
<details>
  <summary>Reflection 1</summary>

Pada tahap pengembangan dengan TDD, saya menemukan bahwa alur TDD memberikan feedback dini yang sangat berguna. Dengan menulis unit test sebelum implementasi, saya dapat mendeteksi masalah desain dan fungsionalitas sejak awal, sehingga membantu saya menghasilkan kode yang lebih modular, maintainable, dan terdokumentasi dengan baik. Pendekatan ini memaksa saya untuk berpikir secara detail tentang setiap fitur yang akan diimplementasikan, sehingga mengurangi kemungkinan terjadinya bug dan memudahkan refactoring.

Namun, saya menyadari bahwa masih ada area yang perlu ditingkatkan, seperti cakupan edge case dan kompleksitas beberapa skenario yang belum teruji secara menyeluruh. Untuk iterasi selanjutnya, saya akan lebih fokus pada peningkatan cakupan pengujian, memastikan setiap branch dan kondisi ekstrem diuji dengan baik, serta mengintegrasikan pipeline CI/CD agar setiap perubahan kode dapat langsung diuji secara otomatis. Hal ini akan membantu memastikan bahwa seluruh fitur, termasuk sub-feature baru, terus berjalan sesuai harapan seiring dengan perkembangan proyek.

</details>

<details>
  <summary>Reflection 2</summary>

Dalam hal penerapan prinsip F.I.R.S.T., saya merasa bahwa unit test yang saya buat sudah cukup cepat, mandiri, dan repeatable, berkat penggunaan mock untuk mengisolasi dependensi. Tes yang saya tulis juga self-validating karena hasil pass/fail-nya langsung memberikan feedback tanpa perlu analisis manual, yang sangat berguna dalam proses debugging dan maintenance. Meskipun demikian, saya menyadari perlunya konsistensi dalam penggunaan data fixture dan stubbing nilai-nilai non-deterministik, agar pengujian selalu menghasilkan output yang konsisten di berbagai lingkungan.

Ke depan, saya akan lebih disiplin dalam menulis tes secara tepat waktu (timely) bersama dengan pengembangan fitur baru dan terus memastikan setiap unit test memiliki asersi yang lengkap serta pesan kesalahan yang informatif. Pendekatan ini akan meningkatkan kualitas keseluruhan suite testing, sehingga tidak hanya mencapai cakupan 100% secara garis besar, tetapi juga mencakup semua kondisi edge-case dan menjamin bahwa setiap fitur berfungsi dengan baik di seluruh iterasi pengembangan.

</details>


