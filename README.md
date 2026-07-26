# MFT Music Player

شروع پروژه‌ی موزیک‌پلیر تیم MFT بر پایه‌ی Android Media3.

## معماری پروژه

معماری پروژه باید بر پایه‌ی **MVI & Clean Architecture** پیاده‌سازی شود. مرزبندی بین لایه‌های Presentation، Domain و Data باید حفظ شود.

## تیم

| نفر | نقش | مسئولیت |
| --- | --- | --- |
| [Amin Sakha](https://github.com/aminsakha) (`@aminsakha`) | Project Manager | مدیریت پروژه، مدیریت ریپو و تصمیم‌های نهایی |
| `behnam` | Project Coordinator | هماهنگی اجرای تسک‌ها، پیگیری وضعیت تیم و راه‌اندازی Media3 و اتصال Player |
| `mohamad ali farajolahi` | Developer | اسکن فایل‌های صوتی و نمایش کتابخانه موسیقی |
| `Shayan armannia` | Developer | پیاده‌سازی پخش صدا و صفحه Player |
| `مهدیار قزوینیان` | Developer | پیاده‌سازی صف پخش و جست‌وجوی آهنگ‌ها |
| `Abolfazl Khalili` | Developer | اعلان Media، آهنگ‌های اخیر و آماده‌سازی انتشار |
| `Parsa` | Developer | آهنگ‌های موردعلاقه، تنظیمات و آماده‌سازی انتشار |
| `Leila Abdi` | UI/UX Designer & Tester | طراحی تجربه و رابط کاربری، تست و کنترل کیفیت |

> این فهرست بر اساس کارت‌هایی است که در برد Trello به اعضا assign شده‌اند. نام‌ها عیناً از Trello استخراج شده‌اند؛ برای منشن GitHub، هندل دقیق هر نفر باید در GitHub مشخص شود.

## استراتژی Git

شاخه‌ی پیش‌فرض ریپو `master` است و فقط نقش release/stable دارد. توسعه‌ی روزمره باید از `develop` انجام شود.

1. همیشه آخرین `develop` را بگیرید:

   ```bash
   git switch develop
   git pull origin develop
   ```

2. برای هر تسک یک شاخه از `develop` بسازید:

   ```bash
   git switch -c feature/<short-task-name>
   ```

3. تغییرات را با commitهای کوچک و معنادار push کنید و یک Pull Request به `develop` باز کنید.
4. در PR، کارت Trello مرتبط، خلاصه‌ی تغییرات و روش تست را بنویسید و **Behnam (`@behnam`)** را برای review منشن کنید؛ همچنین لینک PR و وضعیت آن را در گروه اعلام کنید.
5. تا قبل از تأیید review و پاس‌شدن تست‌ها، PR را merge نکنید. merge به `master` فقط برای release و با هماهنگی مالک پروژه انجام می‌شود.

### نام‌گذاری شاخه‌ها

- `feature/<name>` برای قابلیت جدید
- `fix/<name>` برای رفع باگ
- `refactor/<name>` برای بازآرایی بدون تغییر رفتار
- `chore/<name>` برای کارهای build، dependency و tooling

## وضعیت شاخه‌ها

- `master`: شاخه‌ی پیش‌فرض و پایدار
- `develop`: شاخه‌ی ادغام توسعه

## وضعیت پروژه

این ریپو نقطه‌ی شروع پروژه است. ساختار اپلیکیشن، تست‌ها و CI هم‌زمان با شکستن کارت‌های MVP اضافه می‌شوند.
