# Generated by Django 3.2.8 on 2021-10-21 08:09

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('maize_api', '0010_data_image'),
    ]

    operations = [
        migrations.AlterField(
            model_name='data',
            name='image',
            field=models.ImageField(height_field=224, null=True, upload_to='', width_field=224),
        ),
    ]