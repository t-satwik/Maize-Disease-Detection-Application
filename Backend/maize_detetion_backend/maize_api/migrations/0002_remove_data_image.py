# Generated by Django 3.2.8 on 2021-10-11 06:21

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('maize_api', '0001_initial'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='data',
            name='image',
        ),
    ]
