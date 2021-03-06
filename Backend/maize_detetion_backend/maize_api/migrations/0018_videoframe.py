# Generated by Django 3.2.8 on 2021-11-03 19:41

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('maize_api', '0017_admin'),
    ]

    operations = [
        migrations.CreateModel(
            name='VideoFrame',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('startLatitude', models.DecimalField(blank=True, decimal_places=16, max_digits=22, null=True)),
                ('startLongitude', models.DecimalField(blank=True, decimal_places=16, max_digits=22, null=True)),
                ('encoded_image', models.TextField(null=True)),
                ('predicted_class', models.TextField(null=True)),
                ('image_path', models.TextField(null=True)),
                ('crop_type', models.TextField(null=True)),
                ('probability', models.DecimalField(blank=True, decimal_places=16, max_digits=22, null=True)),
                ('user_name', models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='maize_api.user')),
            ],
        ),
    ]
